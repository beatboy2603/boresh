function getPageContextPath() {
    var path = window.location.pathname;
    var reg = /^(\/[^/\n]*)\/[^/\n]*/;
    var pageContextPath = reg.exec(path)[1];
    return pageContextPath;
}
