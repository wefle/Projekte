const starsLabelFood = document.querySelectorAll('.food_Rate .small_star_container label');
const starsFood = document.querySelectorAll('.food_Rate .small_star_container .stars');
const starDefaultFood = document.querySelector('#zero');

const starsLabelService = document.querySelectorAll('.service_Rate .small_star_container label');
const starsService = document.querySelectorAll('.service_Rate .small_star_container .stars');
const starDefaultService = document.querySelector('#zer_o');

starDefaultFood.checked = true;
starDefaultService.checked = true;
const maxLabel = starsLabelFood.length-1;
var foodValue = serviceValue = 0;

const coloringStars = (labelArray ,value) =>{
    for(var i = 0; i <= value; i++){
        labelArray[i].style.color = "#870000";
        labelArray[i].innerHTML = "★";
    }
    const j = +value + +1;
    for(var i = j; i <= maxLabel; i++){
        labelArray[i].style.color = "#9b9b9b";
        labelArray[i].innerHTML = "☆";
    }
}
starsFood.forEach((star) => {
    star.addEventListener('click', function(){
        coloringStars(starsLabelFood, star.value);
        foodValue = +star.value + +1;
    });
});
starsService.forEach((star) => {
    star.addEventListener('click', function(){
        coloringStars(starsLabelService, star.value);
        serviceValue = +star.value + +1;
    });
});

var lastName = document.getElementById('lastName');
var firstName = document.getElementById('firstName');
var message = document.getElementById('notes');
var submit = document.querySelector('.btn');

const setCookie = (lname, lvalue, exdays) =>{
    const d = new Date();
    const day = d.getDate() < 10 ? (0 + d.getDate()) : d.getDate();
    const month = (+d.getMonth()+ +1) < 10 ? ("0"  + (+d.getMonth()+ +1) ) : (+d.getMonth()+ +1) ;
    const hours = d.getHours() < 10 ? ("0" + d.getHours()) : d.getHours();
    const minutes = d.getMinutes() < 10 ? ("0" + d.getMinutes()) : d.getMinutes();
    console.log(hours);
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    let expires = "expires="+ d.toUTCString();
    document.cookie = lname + "=" + "[" + firstName.value + " " + lvalue + " hat am " + 
    day + "." + month + "." + d.getFullYear() + " um " +
    hours + ":"  + minutes + " Uhr folgende Bewertung abgegeben: " +
    "Essen: " + foodValue + " Sterne, " +
    "Service: " + serviceValue + " Sterne, " +
    "Nachricht: " + message.value + "]" + ";" 
    + expires + ";path=/; SameSite=Lax; Secure";
}
const getCookie = (lname) =>{
    var name = lname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i < ca.length; i++){
        var c = ca[i];
        while(c.charAt(0) == ' '){
            c = c.substring(1);
        }
        if(c.indexOf(name) == 0){
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

const validateForm = () =>{
    if(lastName.value == "" || firstName.value == ""){
        alert("Du musst Vorname und Name angeben!");
        document.location.reload();
        return false;
    }
    else if(lastName.value.length == 1 || firstName.value == 1){
        alert("Es muss ein gültiger Name angegeben werden!");
        document.location.reload();
        return false;
    }
    setCookie(lastName, lastName.value, 1);
    document.querySelector('.popUp').style.visibility = "visible";
    var cookieDetail = document.querySelector('#cookieDetail');
    cookieDetail.append(getCookie(lastName));
}
/*function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}
deleteAllCookies();*/
submit.addEventListener('click', validateForm);

