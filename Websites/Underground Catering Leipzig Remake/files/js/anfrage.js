const catVsStFDots = document.querySelectorAll('.CatVsStF .dots input');
const catVsStFLabels = document.querySelectorAll('.CatVsStF .dots label i');

catVsStFLabels[0].className = "fa-regular fa-circle-dot";
catVsStFLabels[0].style.color = "#870000"; 

catVsStFDots.forEach((dot) =>{
    dot.addEventListener('click', function(){
        if(dot.value == 0){
            catVsStFLabels[0].className = "fa-regular fa-circle-dot";
            catVsStFLabels[0].style.color = "#870000"; 
            catVsStFLabels[1].className = "fa-regular fa-circle";
            catVsStFLabels[1].style.color = "#9b9b9b"; 
        }
        else if(dot.value == 1){
            catVsStFLabels[1].className = "fa-regular fa-circle-dot";
            catVsStFLabels[1].style.color = "#870000"; 
            catVsStFLabels[0].className = "fa-regular fa-circle";
            catVsStFLabels[0].style.color = "#9b9b9b"; 
        }
    });
});

const erklärungInput = document.querySelector('.erklärung_ .dots input');
const erklärungLabel = document.querySelector('.erklärung_ .dots label i');

document.querySelector('#zero_dot').checked = true;

erklärungInput.addEventListener('click', function(){
    erklärungLabel.className = "fa-regular fa-circle-dot";
    erklärungLabel.style.color = "#870000"; 
});
