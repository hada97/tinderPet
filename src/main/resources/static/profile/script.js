// Variáveis globais para controle de navegação
let currentPage = 0; // Página atual (inicialmente, página 0)
let dogsPerPage = 10; // Número de cachorros por página (10 por vez)
let dogsList = []; // Lista de cachorros da página atual
let currentDogIndex = 0; // Índice do cachorro atual na página
let totalPages = 0; // Número total de páginas (inicialmente 0)

const baseUrl = "http://localhost:8080";
const apiUrlDogs = `${baseUrl}/dogs`;

const token = localStorage.getItem("authToken");

const dogName = document.querySelector(".dog__name");
const dogNumber = document.querySelector(".dog__number");
const dogImage = document.querySelector(".dog__image");
const dogDescription = document.querySelector(".dog__description");
const dogAge = document.querySelector(".dog__age");
const dogBreed = document.querySelector(".dog__breed");
const dogSize = document.querySelector(".dog__size");
const dogGender = document.querySelector(".dog__gender");
const dogNeutered = document.querySelector(".dog__neutered");
const form = document.querySelector(".form");


// Função para renderizar os detalhes do cachorro atual
const renderCurrentDog = () => {
  if (dogsList.length > 0 && currentDogIndex < dogsList.length) {
    const dog = dogsList[currentDogIndex];

    dogName.innerHTML = "Loading...";
    dogDescription.innerHTML = "";
    dogAge.innerHTML = "";
    dogBreed.innerHTML = "";
    dogSize.innerHTML = "";
    dogGender.innerHTML = "";
    dogNeutered.innerHTML = "";

    dogImage.style.display = "block";
    dogName.innerHTML = dog.name;
    dogDescription.innerHTML = `Description: ${dog.description}`;
    dogAge.innerHTML = `Age: ${dog.age} years`;
    dogBreed.innerHTML = `Breed: ${dog.breed}`;
    dogSize.innerHTML = `Size: ${dog.size}`;
    dogGender.innerHTML = `Gender: ${dog.gender}`;
    dogNeutered.innerHTML = `Neutered: ${dog.neutered ? "Yes" : "No"}`;
    dogImage.src = dog.profilePictureUrl;
  } else {
    dogImage.style.display = "none";
    dogName.innerHTML = "Not found :c";
    dogNumber.innerHTML = "";
    dogDescription.innerHTML = "";
    dogAge.innerHTML = "";
    dogBreed.innerHTML = "";
    dogSize.innerHTML = "";
    dogGender.innerHTML = "";
    dogNeutered.innerHTML = "";
  }
};


// Função para carregar os cachorros da página atual
const loadDogsPage = async () => {
  const pageData = await fetchDogsPage(currentPage, dogsPerPage);
  dogsList = pageData.content;
  totalPages = pageData.totalPages;  // Atribuir o número total de páginas
  renderCurrentDog();
};



// Carregar os cachorros assim que o DOM estiver pronto
document.addEventListener("DOMContentLoaded", () => {
  loadDogsPage(); // Certifique-se de que o carregamento da página de cachorros acontece apenas depois do DOM estar pronto
});


function logout() {
  alert("Você saiu!");
  window.location.href = "/";
}

fetch("http://localhost:8080/api/user/profile", {
  headers: {
    Authorization: `Bearer ${token}`,
  },
})
  .then((response) => response.json())
  .then((data) => {
    document.getElementById("user-name").innerText = data.name;
  })
  .catch((error) => {
    console.error("Erro ao carregar o nome do usuário:", error);
    document.getElementById("user-name").innerText = "Erro ao carregar o nome";
  });