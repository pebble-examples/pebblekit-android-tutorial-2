#include <pebble.h>

#define KEY_BUTTON_UP   0
#define KEY_BUTTON_DOWN 1

static Window *s_main_window;
static TextLayer *s_output_layer;

static void send(int key, int value) {
  DictionaryIterator *iter;
  app_message_outbox_begin(&iter);

  dict_write_int(iter, key, &value, sizeof(int), true);

  app_message_outbox_send();
}

static void outbox_sent_handler(DictionaryIterator *iter, void *context) {
  // Ready for next command
  text_layer_set_text(s_output_layer, "Press up or down.");
}

static void outbox_failed_handler(DictionaryIterator *iter, AppMessageResult reason, void *context) {
  text_layer_set_text(s_output_layer, "Send failed!");
  APP_LOG(APP_LOG_LEVEL_ERROR, "Fail reason: %d", (int)reason);
}

static void up_click_handler(ClickRecognizerRef recognizer, void *context) {
  text_layer_set_text(s_output_layer, "Up");

  send(KEY_BUTTON_UP, 0);
}

static void down_click_handler(ClickRecognizerRef recognizer, void *context) {
  text_layer_set_text(s_output_layer, "Down");

  send(KEY_BUTTON_DOWN, 0);
}

static void click_config_provider(void *context) {
  window_single_click_subscribe(BUTTON_ID_UP, up_click_handler);
  window_single_click_subscribe(BUTTON_ID_DOWN, down_click_handler);
}

static void main_window_load(Window *window) {
  Layer *window_layer = window_get_root_layer(window);
  GRect bounds = layer_get_bounds(window_layer);

#if defined(PBL_SDK_2)
  s_output_layer = text_layer_create(GRect(0, 60, bounds.size.w, bounds.size.h));
#elif defined(PBL_SDK_3)
  const int text_height = 20;
  const GEdgeInsets text_insets = GEdgeInsets((bounds.size.h - text_height) / 2, 0);

  s_output_layer = text_layer_create(grect_inset(bounds, text_insets));
#endif
  text_layer_set_text(s_output_layer, "Press up or down.");
  text_layer_set_text_alignment(s_output_layer, GTextAlignmentCenter);
  layer_add_child(window_layer, text_layer_get_layer(s_output_layer));
}

static void main_window_unload(Window *window) {
  text_layer_destroy(s_output_layer);
}

static void init(void) {
  s_main_window = window_create();
  window_set_click_config_provider(s_main_window, click_config_provider);
  window_set_window_handlers(s_main_window, (WindowHandlers) {
    .load = main_window_load,
    .unload = main_window_unload,
  });
  window_stack_push(s_main_window, true);

  // Open AppMessage
  app_message_register_outbox_sent(outbox_sent_handler);
  app_message_register_outbox_failed(outbox_failed_handler);
  app_message_open(app_message_inbox_size_maximum(), app_message_outbox_size_maximum());
}

static void deinit(void) {
  window_destroy(s_main_window);
}

int main(void) {
  init();
  app_event_loop();
  deinit();
}
