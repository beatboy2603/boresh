$(document).ready(function() {
    $("#form-search").submit(function(e) {
        e.preventDefault();
        doSearch();
    });
});

function doSearch() {
    var iptKeyword = $("#ipt-keyword").val();
    
    ajaxGoogleBookMulti(iptKeyword, printResults);
}

function printResults(e) {
    var bookList = e.items;
    if(!bookList) {
        $("#field-book-result").html("<p>No result found</p>");
    } else {
        clearBookResultField();
        
        console.log(bookList.length);
        
        var bookItem;
        var thumbnail;
        for(var i=0; i<bookList.length; ++i) {
            bookItem = bookList[i];
            if(bookItem.volumeInfo.imageLinks) {
                thumbnail = bookItem.volumeInfo.imageLinks.thumbnail;
            } else {
                thumbnail = "none";
            }
            createBookItem(bookItem.id, bookItem.volumeInfo.title, thumbnail);
        }
    }
}

function clearBookResultField() {
    $("#field-book-result").html("");
}

/**
 * Create a DOM element, a book item for each result from searching
*/
function createBookItem(id, title, thumbnail) {
    var itemWrapperElem = "<div id=" + id + " class='wrapper-book'></div>";
    $("#field-book-result").append(itemWrapperElem);
    
    var titleElem = "<div><a href='book/" + id + "'>" + title + "</a></div>";
    
    if(thumbnail !== "none") {
        var thumbnailElem = "<img src='" + thumbnail + "'>";
        $("#" + id).append(titleElem, thumbnailElem);
    } else {
        $("#" + id).append(titleElem);
    }
}

