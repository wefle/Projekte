const mobile_menu = document.querySelector('#mobile_menu');
const menu_content = document.querySelector('.menu');

const mobileMenu = () =>{
    mobile_menu.classList.toggle('is_active');
    menu_content.classList.toggle('active');
}

mobile_menu.addEventListener('click', mobileMenu);
document.querySelector('.header_bar').style.position = "relative";

const stickOn = () =>{
    if(window.scrollY > 200){
        document.querySelector('.header_bar').style.background = 
        "linear-gradient(to bottom,rgba(51, 51, 51, 0.85), #000)";
        document.querySelector('.header_bar').style.position = "fixed";
        document.querySelector('.header_bar').style.top = "0";
        document.querySelector('#Home_Logo_small').style.display = "inline";
    }
    else{
        document.querySelector('#Home_Logo_small').style.display = "none";
        document.querySelector('.header_bar').style.background = "transparent";
        document.querySelector('.header_bar').style.position = "relative";
    }
}

window.addEventListener('scroll', stickOn);

const hideMobilMenu = () =>{
    if(window.innerWidth <= 960){
        mobile_menu.classList.toggle('is_active');
        menu_content.classList.remove('active');
    }
}
const menuLinks = document.querySelectorAll('.menu_links');
menuLinks.forEach((menuLink) =>{
    menuLink.addEventListener('click', hideMobilMenu);
});
