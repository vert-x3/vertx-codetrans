require 'json'
java_import 'io.vertx.core.json.JsonObject'
java_import 'io.vertx.core.json.JsonArray'
module Support
  class JsonConverter
    def self.to_json_object obj
      return JsonObject.new(JSON.generate(obj))
    end
    def self.to_json_array arr
      return JsonArray.new(JSON.generate(arr))
    end
    def self.from_json_array arr
      return JSON.parse(arr.encode)
    end
  end
end
