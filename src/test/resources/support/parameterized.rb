module Support
  class Parameterized
    def self.create val
      Parameterized.new val
    end
    def initialize(val)
      @val = val;
    end
    def get
      @val
    end
  end
end
