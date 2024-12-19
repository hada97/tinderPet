const baseUrl = "http://localhost:8080";
const apiUrlCachorros = `${baseUrl}/dogs`;

// Pega o token de autenticação armazenado no localStorage
const token = localStorage.getItem('authToken');

// Elementos do DOM
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

// Variável para controlar a busca do cachorro
let searchDog = 1;

// Função para buscar um cachorro específico
const fetchDog = async (dogId) => {
  const APIResponse = await fetch(`http://localhost:8080/dogs/${dogId}`, {
    headers: {
      "Authorization": `Bearer ${token}`, // Envia o token no cabeçalho
    }
  });

  if (APIResponse.status === 200) {
    const data = await APIResponse.json();
    return data;
  } else {
    return {
      name: "Pit",
      id: "0",
      description: "No description available",
      age: "",
      breed: "",
      size: "Medium",
      gender: "Unknown",
      neutered: false,
      profilePictureUrl: "default.png",
    };
  }
};

// Função para renderizar o cachorro
const renderDog = async (dogId) => {
  dogName.innerHTML = "Loading...";
  dogDescription.innerHTML = "";
  dogAge.innerHTML = "";
  dogBreed.innerHTML = "";
  dogSize.innerHTML = "";
  dogGender.innerHTML = "";
  dogNeutered.innerHTML = "";

  const data = await fetchDog(dogId);

  if (data) {
    dogImage.style.display = "block";
    dogName.innerHTML = data.name;
    if (dogNumber) {
      dogNumber.innerHTML = `ID: ${data.id}`;
    }
    dogDescription.innerHTML = `Description: ${data.description}`;
    dogAge.innerHTML = `Age: ${data.age} years`;
    dogBreed.innerHTML = `Breed: ${data.breed}`;
    dogSize.innerHTML = `Size: ${data.size}`;
    dogGender.innerHTML = `Gender: ${data.gender}`;
    dogNeutered.innerHTML = `Neutered: ${data.neutered ? "Yes" : "No"}`;
    dogImage.src = data.profilePictureUrl;
    input.value = "";
    searchDog = data.id;
  } else {
    dogImage.style.display = "none";
    dogName.innerHTML = "Not found :c";
    if (dogNumber) {
      dogNumber.innerHTML = "";
    }
    dogDescription.innerHTML = "";
    dogAge.innerHTML = "";
    dogBreed.innerHTML = "";
    dogSize.innerHTML = "";
    dogGender.innerHTML = "";
    dogNeutered.innerHTML = "";
  }
};

// Lidar com o envio do formulário de busca
form.addEventListener("submit", (event) => {
  event.preventDefault();
  renderDog(input.value.toLowerCase());
});

// Navegação entre os cachorros
buttonPrev.addEventListener("click", () => {
  if (searchDog > 1) {
    searchDog -= 1;
    renderDog(searchDog);
  }
});

buttonNext.addEventListener("click", () => {
  searchDog += 1;
  renderDog(searchDog);
});

// Inicializar a exibição do cachorro
renderDog(searchDog);

// Lógica para alternar entre o ícone de adicionar e o formulário
addIcon.addEventListener("click", () => {
  addIcon.style.display = "none";
  addForm.style.display = "block";
});

// Lidar com o envio do formulário de cadastro de cachorro
document.getElementById("dogForm").addEventListener("submit", async function (event) {
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

  if (
    !name ||
    !breed ||
    !gender ||
    !size ||
    !profilePictureUrl ||
    !userId ||
    isNaN(age) ||
    age < 0
  ) {
    alert("Please fill out all fields correctly.");
    return;
  }

  try {
    const response = await fetch(apiUrlCachorros, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`, // Envia o token de autenticação
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
      document.getElementById("addForm").style.display = "none";
    } else {
      const data = await response.json();
      alert("Error: " + data.message);
    }
  } catch (error) {
    alert("An error occurred while trying to register the dog: " + error.message);
  }
});

// Função para mostrar ou esconder o loader
function toggleLoader(show) {
  const preloader = document.getElementById("preloader");
  const loader = document.getElementById("loader");
  if (show) {
    preloader.style.display = "flex";
    loader.style.display = "block";
  } else {
    preloader.style.display = "none";
    loader.style.display = "none";
  }
}

