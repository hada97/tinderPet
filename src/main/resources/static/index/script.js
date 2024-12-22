
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
    dogCard.setAttribute("data-id", dog.id);

  }
};

// Função para buscar os cachorros da página atual
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
      console.log("Dados da API:", data); // Verifique os dados da API

      // Extrair os dados da resposta
      return {
        content: data.content,          // Lista de cachorros
        totalPages: data.page.totalPages  // Total de páginas
      };
    } else {
      const errorData = await APIResponse.json();
      console.error(
        `Erro: ${APIResponse.status} - ${errorData.message || APIResponse.statusText}`
      );
      return { content: [], totalPages: 0 }; // Retorna lista vazia e total de páginas 0
    }
  } catch (error) {
    console.error("Erro inesperado: ", error);
    return { content: [], totalPages: 0 }; // Retorna lista vazia e total de páginas 0
  }
};



// Função para renderizar a navegação de cachorros
const navigateDog = (direction) => {
  if (direction === "next") {

    // Verificar se há mais cachorros na página atual
    if (currentDogIndex < dogsList.length - 1) {
      currentDogIndex += 1; // Avançar para o próximo cachorro
      renderCurrentDog();
    } else if (currentPage < totalPages - 1) {
      // Verificar se há mais páginas
      currentPage += 1; // Avançar para a próxima página
      currentDogIndex = 0; // Resetar para o primeiro cachorro da nova página
      loadDogsPage(); // Carregar os cachorros da nova página
    }
  } else if (direction === "prev") {
    // Verificar se há cachorros anteriores na página atual
    if (currentDogIndex > 0) {
      currentDogIndex -= 1; // Retroceder para o cachorro anterior
      renderCurrentDog();
    } else if (currentPage > 0) {
      // Verificar se há páginas anteriores
      currentPage -= 1; // Retroceder para a página anterior
      currentDogIndex = dogsPerPage - 1; // Definir o último cachorro da página anterior
      loadDogsPage(); // Carregar os cachorros da página anterior
    }
  }
};


// Função para carregar os cachorros da página atual
const loadDogsPage = async () => {
  const pageData = await fetchDogsPage(currentPage, dogsPerPage);
  dogsList = pageData.content;
  totalPages = pageData.totalPages;  // Atribuir o número total de páginas
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
  loadDogsPage(); // Certifique-se de que o carregamento da página de cachorros acontece apenas depois do DOM estar pronto
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




document.querySelectorAll('.button.btn-right').forEach(button => {
  button.addEventListener('click', async function() {
    const dogCardContainer = this.closest(".dog-container"); // Ajuste para pegar o card correto
    const dogId = dogCardContainer.getAttribute("data-id");
    console.log("DOG ID:", dogId);

    try {
      // Obter o ID do usuário
      const responseUserId = await fetch(`${baseUrl}/users/login/id`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (responseUserId.ok) {
        const userId = await responseUserId.json();
        console.log("User ID:", userId);

        // Fazer a requisição para curtir o cachorro
        const responseLike = await fetch(`${baseUrl}/dogs/${dogId}/like/${userId}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
        });

        // Caso a curtida tenha dado certo, avança para o próximo cachorro
        navigateDog("next");
        console.log("lIKE REGISTRADO");

      } else {
        throw new Error("Falha ao obter o userId");
      }
    } catch (error) {
      console.error("Erro ao curtir:", error);
    }
  });
});
