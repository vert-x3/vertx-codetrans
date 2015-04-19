module Support
  class HandlerInvoker
    def self.invoke_string_handler
      yield 'callback_value'
    end
    def self.invoke_string_handler_first_param handler, other
      handler.call other
    end
    def self.invoke_string_handler_last_param other
      yield other
    end
    def self.invoke_async_result_handler_success(&callback)
      callback.call 'hello', nil
    end
    def self.invoke_async_result_handler_failure(&callback)
      future = Java::IoVertxCore::Future.failedFuture 'oh no'
      callback.call nil, future.cause
    end
  end
end
