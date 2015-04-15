module Support
  class SubHandler
    def initialize()
    end
    def handle(event)
      Java::IoVertxCodetrans::MethodExpressionTest.event = event
    end
    def instance_handler(handler)
      handler.handle 'hello_instance'
    end
    def self.create
      SubHandler.new
    end
    def self.class_handler(handler)
      handler.handle 'hello_class'
    end
  end
end
