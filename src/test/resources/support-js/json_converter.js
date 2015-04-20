var JsonObject = Packages.io.vertx.core.json.JsonObject;
var JsonArray = Packages.io.vertx.core.json.JsonArray;
module.exports = {
    toJsonObject: function(obj) {
        return new JsonObject(JSON.stringify(obj));
    },
    toJsonArray: function(obj) {
        return new JsonArray(JSON.stringify(obj));
    }
};