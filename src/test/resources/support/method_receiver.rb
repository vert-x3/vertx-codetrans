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
    def self.parameterized_method_matching_type_variable_parameter(arg)
      if arg.nil?
        return 'hello'
      else
        return arg
      end
    end
    def self.parameterized_method_matching_generic_parameter(arg, &callback)
      if arg.nil?
        return 'hello'
      else
        return arg.get
      end
    end
  end
end
