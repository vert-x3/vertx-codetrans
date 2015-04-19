module Support
  class CollectionFactory
    def self.create_map
      hash = Hash.new
      hash['foo'] = 'foo_value'
      return hash
    end
  end
end
