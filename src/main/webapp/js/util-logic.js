
function randomID() {
    var newID = Date.now();
    var randLetter = String.fromCharCode(65 + Math.floor(Math.random() * 26));
    newID = randLetter+newID;

    return newID;
}


