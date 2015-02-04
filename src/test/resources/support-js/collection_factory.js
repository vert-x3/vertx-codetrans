module.exports = {
    createMap: function() {
        return JSAdapter({
            __get__: function (key) {
                if (key == "foo") {
                    return "foo_value";
                }
                return null;
            },
            __call__: function (name, arg1) {
                switch (name) {
                    case 'forEach':
                        arg1("foo_value", "foo");
                        break;
                    default:
                        throw new TypeError(name + " not implemented");
                }
            }
        });
    }
};