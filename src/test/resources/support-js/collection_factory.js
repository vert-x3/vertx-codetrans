module.exports = {
    createMap: function() {
        return JSAdapter({
            __get__: function (key) {
                if (key == "foo") {
                    return "foo_value";
                }
                return null;
            }
        });
    }
};