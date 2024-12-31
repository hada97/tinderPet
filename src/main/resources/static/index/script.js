let currentPage = 0;
let dogsPerPage = 10;
let dogsList = [];
let currentDogIndex = 0;
let totalPages = 0;
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
const dogCard = document.querySelector(".dog-container");

// Renderiza os detalhes do cachorro atual
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
    dogCard.setAttribute("data-id", dog.id);
  }
};

// Busca os cachorros da página atual
const fetchDogsPage = async (page, size) => {
  try {
    const APIResponse = await fetch(
      `http://localhost:8080/dogs?page=${page}&size=${size}`,
      {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (APIResponse.status === 200) {
      const data = await APIResponse.json();
      console.log("Dados da API:", data);

      return {
        content: data.content,
        totalPages: data.page.totalPages,
      };
    } else {
      const errorData = await APIResponse.json();
      console.error(
        `Erro: ${APIResponse.status} - ${
          errorData.message || APIResponse.statusText
        }`
      );
      return { content: [], totalPages: 0 };
    }
  } catch (error) {
    console.error("Erro inesperado: ", error);
    return { content: [], totalPages: 0 };
  }
};

// Navegação entre os cachorros
const navigateDog = (direction) => {
  if (direction === "next") {
    if (currentDogIndex < dogsList.length - 1) {
      currentDogIndex += 1;
      renderCurrentDog();
    } else if (currentPage < totalPages - 1) {
      currentPage += 1;
      currentDogIndex = 0;
      loadDogsPage();
    }
  } else if (direction === "prev") {
    if (currentDogIndex > 0) {
      currentDogIndex -= 1;
      renderCurrentDog();
    } else if (currentPage > 0) {
      currentPage -= 1;
      currentDogIndex = dogsPerPage - 1;
      loadDogsPage();
    }
  }
};

// Carrega os cachorros da página atual
const loadDogsPage = async () => {
  const pageData = await fetchDogsPage(currentPage, dogsPerPage);
  dogsList = pageData.content;
  totalPages = pageData.totalPages;
  renderCurrentDog();
};

addIcon.addEventListener("click", () => {
  addIcon.style.display = "none";
  addForm.style.display = "block";
});

buttonNext.addEventListener("click", () => navigateDog("next"));

buttonPrev.addEventListener("click", () => navigateDog("prev"));

// Inicialização ao carregar o DOM
document.addEventListener("DOMContentLoaded", () => {
  loadDogsPage();
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
    const profilePictureUrl =
      document.getElementById("profilePictureUrl").value;
    const description = document.getElementById("description").value;
    const isNeutered = document.getElementById("isNeutered").checked;

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
        }),
      });

      if (response.ok) {
        alert("Dog successfully registered!");
        document.getElementById("dogForm").reset();
        addForm.style.display = "none";
        addIcon.style.display = "block";
        await loadDogsPage();
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

document.querySelectorAll(".button.btn-right").forEach((button) => {
  button.addEventListener("click", async function () {
    const dogCardContainer = this.closest(".dog-container");
    const dogId = dogCardContainer.getAttribute("data-id");
    console.log("DOG ID:", dogId);

    try {
      const responseUserId = await fetch(`${baseUrl}/users/login/id`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (responseUserId.ok) {
        const userId = await responseUserId.json();
        console.log("User ID:", userId);

        const responseLike = await fetch(`${baseUrl}/like/${dogId}/${userId}`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });
        navigateDog("next");
        console.log("lIKE REGISTRADO");
        await loadDogsPage();
      } else {
        throw new Error("Falha ao obter o userId");
      }
    } catch (error) {
      console.error("Erro ao curtir:", error);
    }
  });
});
