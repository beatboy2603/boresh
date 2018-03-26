// Need jquery src above

function ajaxGoogleBookSingle(bookID, callback) {
    $.ajax({
        url: "https://www.googleapis.com/books/v1/volumes/"+bookID+"?key=AIzaSyCzu9ToCyZhU4czoux80MyDv1BD_-Vw4FQ",
        type: "GET",
        dataType: "json",
        success: callback
    });
}

function ajaxGoogleBookMulti(keyword, callback) {
    $.ajax({
        url: "https://www.googleapis.com/books/v1/volumes?q="+keyword+"&key=AIzaSyCzu9ToCyZhU4czoux80MyDv1BD_-Vw4FQ",
        type: "GET",
        dataType: "json",
        success: callback
    });
}
