var JsonObject = Packages.io.vertx.core.json.JsonObject;
var JsonArray = Packages.io.vertx.core.json.JsonArray;
module.exports = {
    toJsonObject: function(obj) {
        return new JsonObject(JSON.stringify(obj));
    },
    toJsonArray: function(arr) {
        return new JsonArray(JSON.stringify(arr));
    },
    fromJsonObject: function(obj) {
        return JSON.parse(obj.encode());
    },
    fromJsonArray: function(arr) {
        return JSON.parse(arr.encode());
    }
};