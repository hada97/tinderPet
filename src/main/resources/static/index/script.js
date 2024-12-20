// Variáveis globais para controle de navegação
let currentPage = 0;  // Página atual (inicialmente, página 0)
let dogsPerPage = 10; // Número de cachorros por página (10 por vez)
let dogsList = []; // Lista de cachorros da página atual
let currentDogIndex = 0; // Índice do cachorro atual na página

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
const input = document.querySelector(".input__search");
const buttonPrev = document.querySelector(".btn-prev");
const buttonNext = document.querySelector(".btn-next");
const addIcon = document.getElementById("addIcon");
const addForm = document.getElementById("addForm");

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
    input.value = "";
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

// Função para buscar os cachorros da página atual
const fetchDogsPage = async (page, size) => {
  try {
    const APIResponse = await fetch(`http://localhost:8080/dogs?page=${page}&size=${size}`, {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (APIResponse.status === 200) {
      const data = await APIResponse.json();
      console.log("Dados da API:", data); // Verifique os dados da API
      return data; // Verifica se 'data.content' tem os cachorros
    } else {
      const errorData = await APIResponse.json();
      console.error(
        `Erro: ${APIResponse.status} - ${errorData.message || APIResponse.statusText}`
      );
      return { content: [] };  // Retorna uma lista vazia em caso de erro
    }
  } catch (error) {
    console.error("Erro inesperado: ", error);
    return { content: [] };  // Retorna uma lista vazia em caso de erro
  }
};

// Função para renderizar a navegação de cachorros
const navigateDog = (direction) => {
  if (direction === "next") {
    console.log("Cachorros carregados:", dogsList); // Verifique os cachorros carregados
    // Verificar se há mais cachorros na página atual
    if (currentDogIndex < dogsList.length - 1) {
      currentDogIndex += 1;  // Avançar para o próximo cachorro
      renderCurrentDog();
    } else if (currentPage < page.totalPages - 1) {  // Verificar se há mais páginas
      currentPage += 1;  // Avançar para a próxima página
      currentDogIndex = 0;  // Resetar para o primeiro cachorro da nova página
      loadDogsPage();  // Carregar os cachorros da nova página
    }
  } else if (direction === "prev") {
    // Verificar se há cachorros anteriores na página atual
    if (currentDogIndex > 0) {
      currentDogIndex -= 1;  // Retroceder para o cachorro anterior
      renderCurrentDog();
    } else if (currentPage > 0) {  // Verificar se há páginas anteriores
      currentPage -= 1;  // Retroceder para a página anterior
      currentDogIndex = dogsPerPage - 1;  // Definir o último cachorro da página anterior
      loadDogsPage();  // Carregar os cachorros da página anterior
    }
  }
};

// Função para carregar os cachorros da página atual
const loadDogsPage = async () => {
  const pageData = await fetchDogsPage(currentPage, dogsPerPage);
  dogsList = pageData.content;
  console.log("Cachorros carregados:", dogsList); // Verifique os cachorros carregados
  currentDogIndex = 0;
  renderCurrentDog();
};

addIcon.addEventListener("click", () => {
  addIcon.style.display = "none";
  addForm.style.display = "block";
});

buttonNext.addEventListener("click", () => navigateDog("next"));

buttonPrev.addEventListener("click", () => navigateDog("prev"));

// Carregar os cachorros assim que o DOM estiver pronto
document.addEventListener("DOMContentLoaded", () => {
  loadDogsPage();  // Certifique-se de que o carregamento da página de cachorros acontece apenas depois do DOM estar pronto
});

// Formulário para registrar um novo cachorro
document
  .getElementById("dogForm")
  .addEventListener("submit", async function (event) {
    event.preventDefault();
    const name = document.getElementById("name").value;
    const age = parseInt(document.getElementById("age").value);
    const breed = document.getElementById("breed").value;
    const gender = document.getElementById("gender").value;
    const size = document.getElementById("size").value;
    const profilePictureUrl = document.getElementById("profilePictureUrl").value;
    const description = document.getElementById("description").value;
    const isNeutered = document.getElementById("isNeutered").checked;
    const userId = document.getElementById("userId").value;

    const token = localStorage.getItem("access_token");

    try {
      const response = await fetch(apiUrlDogs, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          name,
          age,
          breed,
          gender,
          size,
          profilePictureUrl,
          description,
          isNeutered,
          userId,
        }),
      });

      if (response.ok) {
        alert("Dog successfully registered!");
        document.getElementById("dogForm").reset();
      } else {
        const data = await response.json();
        alert("Error: " + data.message);
      }
    } catch (error) {
      alert(
        "An error occurred while trying to register the dog: " + error.message
      );
    }
  });

