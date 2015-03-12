var io = Packages.io;
var JBuffer = io.vertx.core.buffer.Buffer;
var Buffer = function(j_val) {
    var j_buffer = j_val;
    var that = this;
    this.appendString = function(str) {
        j_buffer.appendString(str);
        return that;
    };
    this.toString = function(enc) {
        return j_buffer.toString(enc);
    };
};
Buffer.buffer = function(s) {
    return new Buffer(JBuffer.buffer(s));
};
module.exports = Buffer;