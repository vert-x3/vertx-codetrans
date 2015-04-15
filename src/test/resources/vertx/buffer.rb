module Vertx
  class Buffer
    def initialize(j_del)
      @j_del = j_del
    end
    def self.buffer(s)
      ::Vertx::Buffer.new((Java::IoVertxCoreBuffer::Buffer.buffer(s)))
    end
    def append_string(str)
      @j_del.appendString(str)
    end
    def to_string(enc)
      @j_del.toString(enc)
    end
  end
end
