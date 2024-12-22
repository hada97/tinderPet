let currentPage = 0; // Página atual (inicialmente, página 0)
let dogsPerPage = 5; // Número de cachorros por página (5 por vez)
let dogsList = []; // Lista de cachorros da página atual
let totalPages = 0; // Número total de páginas (inicialmente 0)
let apiUrlDogs = "";
const token = localStorage.getItem("authToken");
const dogContainer = document.querySelector(".dog-container");
const formTemplate = document.querySelector("#dog-form-template");
const userProfileName = document.getElementById("user-name");
const baseUrl = "http://localhost:8080";

//Função para carregar os dogs da API
const loadDogsPage = async (userId) => {
  try {
    apiUrlDogs = `${baseUrl}/users/${userId}/dogs`;

    const response = await fetch(
      `${apiUrlDogs}?page=${currentPage}&size=${dogsPerPage}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    const pageData = await response.json();
    dogsList = pageData.content; // dogs retornados da API
    totalPages = pageData.totalPages; // Número total de páginas

    console.log("Cachorros carregados:", dogsList); // Aqui você exibe a lista de cachorros

    if (dogsList.length === 0) {
      dogContainer.innerHTML = "<p>No dogs available</p>";
      return;
    }

    dogsList.forEach(renderDogCard); // Renderiza os cachorros em cards
  } catch (error) {
    console.error("Erro ao carregar cachorros:", error);
  }
};

// Função para carregar os dados do usuário
const loadUserProfile = async () => {
  try {
    const response = await fetch("http://localhost:8080/api/user/profile", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error("Falha ao carregar o perfil do usuário");
    }

    const data = await response.json();
    userProfileName.innerText = data.name;

    const responseUserId = await fetch(`${baseUrl}/users/login/id`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (responseUserId.ok) {
      const userId = await responseUserId.json();
      console.log("User ID:", userId);
      loadDogsPage(userId);
    } else {
      throw new Error("Falha ao obter o userId");
    }
  } catch (error) {
    console.error("Erro ao carregar os dados do usuário:", error);
    userProfileName.innerText = "Erro ao carregar o nome";
  }
};

// Carregar os dogs e o perfil do usuário assim que o DOM estiver pronto
document.addEventListener("DOMContentLoaded", () => {
  loadUserProfile(); // Carrega o perfil do user loadDogsPage com o ID
});

// Função de logout
function logout() {
  alert("Você saiu!");
  window.location.href = "/"; // Redireciona para a página de logout
}

//Render DOG
const renderDogCard = (dog) => {
  const dogCard = document.importNode(formTemplate.content, true);
  const dogName = dogCard.querySelector(".dog__name");
  const dogImage = dogCard.querySelector(".dog__image");
  const dogDescription = dogCard.querySelector(".dog__description");
  const dogAge = dogCard.querySelector(".dog__age");
  const dogBreed = dogCard.querySelector(".dog__breed");
  const dogSize = dogCard.querySelector(".dog__size");
  const dogGender = dogCard.querySelector(".dog__gender");
  const dogNeutered = dogCard.querySelector(".dog__neutered");
  dogName.innerHTML = dog.name;
  dogDescription.innerHTML = `Description: ${dog.description}`;
  dogAge.innerHTML = `Age: ${dog.age} years`;
  dogBreed.innerHTML = `Breed: ${dog.breed}`;
  dogSize.innerHTML = `Size: ${dog.size}`;
  dogGender.innerHTML = `Gender: ${dog.gender}`;
  dogNeutered.innerHTML = `Neutered: ${dog.neutered ? "Yes" : "No"}`;
  dogImage.src = dog.profilePictureUrl;

  const dogCardContainer = dogCard.querySelector(".dog-container");
  dogCardContainer.setAttribute("data-id", dog.id);

  const deleteButton = dogCard.querySelector(".btn-left");
  const editButton = dogCard.querySelector(".btn-right");

  deleteButton.addEventListener("click", () => {
    const dogId = dogCardContainer.getAttribute("data-id");
    deleteDog(dogId);
  });

  const mainContainer = document.querySelector(".main-container");
  mainContainer.appendChild(dogCard);
};

// Função de exclusão com confirmação
const deleteDog = (dogId) => {
  const confirmation = confirm("Are you sure you want to delete this dog?");
  if (confirmation) {
    fetch(`${baseUrl}/dogs/${dogId}`, {
      method: "DELETE",
    })
      .then((response) => {
        if (response.ok) {
          console.log(`Dog with ID ${dogId} deleted successfully`);

          const dogCard = document.querySelector(`[data-id="${dogId}"]`);
          if (dogCard) dogCard.remove();
        } else {
          console.error("Error deleting the dog");
        }
      })
      .catch((error) => {
        console.error("Request failed", error);
      });
  } else {
    console.log("Deletion cancelled.");
  }
};



