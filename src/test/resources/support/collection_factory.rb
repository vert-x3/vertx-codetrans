module Support
  class CollectionFactory
    def self.create_map
      hash = Hash.new
      hash['foo'] = 'foo_value'
      return hash
    end
    class Wrapper < Hash
      def initialize(del)
        @del = del;
      end
      def []=(key,val)
        @del.put(key, val)
        super(key,val)
      end
    end
    def self.wrap_map(del)
      return Wrapper.new(del)
    end
  end
end
