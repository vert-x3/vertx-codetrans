module Support
  class MethodReceiver
    def initialize
    end
    def self.red?
      true
    end
    def self.blue?
      false
    end
    def self.parameterized_method(&callback)
      callback.call 'hello'
    end
  end
end
