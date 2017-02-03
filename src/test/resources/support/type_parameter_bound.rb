module Support
  class TypeParameterBound
    def self.create val
      TypeParameterBound.new val
    end
    def initialize(val)
      @val = val;
    end
    def get
      @val
    end
  end
end
