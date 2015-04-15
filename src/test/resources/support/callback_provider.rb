module Support
  class CallbackProvider
    def self.callback_with_string(&handler)
      handler.call 'world'
    end
  end
end
