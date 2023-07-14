const slides = document.querySelectorAll(".slide");

slides.forEach((slide, indx) => {
    slide.style.transform = `translateX(${indx * 100}%)`;
});

let curSlide = 0;
let maxSlide = slides.length - 1;

const carousel = () =>{
    if (curSlide === maxSlide) {
        curSlide = 0;
    } else {
        curSlide++;
    }
    slides[0].style.transform = `translateX(${100 * (0 - curSlide)}%)`; 
    slides[1].style.transform = `translateX(${100 * (1 - curSlide)}%)`;
    slides[2].style.transform = `translateX(${100 * (2 - curSlide)}%)`;
    slides[3].style.transform = `translateX(${100 * (3 - curSlide)}%)`;   
}

var timer;
var observer = new IntersectionObserver(function(entries) {
    if(entries[0].isIntersecting === true){
        /*console.log('Observer sees me partially');*/
        timer = setInterval(carousel, 3600);
    }   
    else{
        /*console.log('Observer cannot see me');*/
        clearInterval(timer);
    }   
}, {threshold: [0]});
observer.observe(document.querySelector('.catering_img_galary'));

const pics = document.querySelectorAll('.pics');
pics.forEach((pic, indx) => {
    pic.style.transform = `translateX(${indx * 100}%)`;
});
const lastPic = document.querySelector('#lastPic');
const foodBox = document.querySelector('.food_box');

const addBlink = () =>{
    pics[0].classList.add('blink');
    pics[1].classList.add('blinkR1');
    pics[2].classList.add('blinkR2');
    pics[3].classList.add('blinkR3');
}
const removeBlink = () =>{
    pics[0].classList.remove('blink');
    pics[1].classList.remove('blinkR1');
    pics[2].classList.remove('blinkR2');
    pics[3].classList.remove('blinkR3');
}
const addBlendInOut = () =>{
    lastPic.classList.add('fadeIn');
    foodBox.classList.add('fadeOut');
}
const removeBlendInOut = () =>{
    foodBox.classList.remove('fadeOut');
    lastPic.classList.remove('fadeIn');
}

document.querySelector('.streetFood_img_galary').addEventListener('mouseover', function(){
    setTimeout(addBlink(), 1000);
});

document.querySelector('.streetFood_img_galary').addEventListener('mouseout', function(){
    setTimeout(addBlendInOut, 4000);
    setTimeout(removeBlink, 5000);
    setTimeout(removeBlendInOut, 6000);
});

