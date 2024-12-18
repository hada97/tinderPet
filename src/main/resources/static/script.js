const baseUrl =
  "http://localhost:8080";
const apiUrlCachorros = `${baseUrl}/dogs`;


const dogName = document.querySelector('.dog__name');
const dogNumber = document.querySelector('.dog__number');
const dogImage = document.querySelector('.dog__image');
const dogDescription = document.querySelector('.dog__description');
const dogAge = document.querySelector('.dog__age');
const dogBreed = document.querySelector('.dog__breed');
const dogSize = document.querySelector('.dog__size');
const dogGender = document.querySelector('.dog__gender');
const dogNeutered = document.querySelector('.dog__neutered');

const form = document.querySelector('.form');
const input = document.querySelector('.input__search');
const buttonPrev = document.querySelector('.btn-prev');
const buttonNext = document.querySelector('.btn-next');

let searchDog = 1;

const fetchDog = async (dog) => {
  const APIResponse = await fetch(`http://localhost:8080/dogs/${searchDog}`);

  if (APIResponse.status === 200) {
    const data = await APIResponse.json();
    return data;
  } else {
    // Se o status não for 200, vamos retornar um cachorro fictício com dados padrão
    return {
      name: 'Pit',
      id: '0',
      description: 'No description available',
      age: '',
      breed: '',
      size: 'Medium',
      gender: 'Unknown',
      neutered: false,
      profilePictureUrl: 'default.png', // Caminho para a imagem padrão
    };
  }
};


const renderDog = async (dog) => {
  dogName.innerHTML = 'Loading...';
  dogNumber.innerHTML = '';
  dogDescription.innerHTML = '';
  dogAge.innerHTML = '';
  dogBreed.innerHTML = '';
  dogSize.innerHTML = '';
  dogGender.innerHTML = '';
  dogNeutered.innerHTML = '';

  const data = await fetchDog(dog);

  if (data) {
    dogImage.style.display = 'block';
    dogName.innerHTML = data.name;
    dogNumber.innerHTML = `ID: ${data.id}`;
    dogDescription.innerHTML = `Description: ${data.description}`;
    dogAge.innerHTML = `Age: ${data.age} years`;
    dogBreed.innerHTML = `Breed: ${data.breed}`;
    dogSize.innerHTML = `Size: ${data.size}`;
    dogGender.innerHTML = `Gender: ${data.gender}`;
    dogNeutered.innerHTML = `Neutered: ${data.neutered ? 'Yes' : 'No'}`;
    dogImage.src = data.profilePictureUrl; // Usa a URL da imagem do cão
    input.value = '';
    searchDog = data.id;
  } else {
    dogImage.style.display = 'none';
    dogName.innerHTML = 'Not found :c';
    dogNumber.innerHTML = '';
    dogDescription.innerHTML = '';
    dogAge.innerHTML = '';
    dogBreed.innerHTML = '';
    dogSize.innerHTML = '';
    dogGender.innerHTML = '';
    dogNeutered.innerHTML = '';
  }
}

form.addEventListener('submit', (event) => {
  event.preventDefault();
  renderDog(input.value.toLowerCase());
});

buttonPrev.addEventListener('click', () => {
  if (searchDog > 1) {
    searchDog -= 1;
    renderDog(searchDog);
  }
});

buttonNext.addEventListener('click', () => {
  searchDog += 1;
  renderDog(searchDog);
});

renderDog(searchDog);


// Lógica para alternar entre o ícone de adicionar e o formulário
const addIcon = document.getElementById('addIcon');
const addForm = document.getElementById('addForm');

addIcon.addEventListener('click', () => {
  addIcon.style.display = 'none';  // Esconde o ícone
  addForm.style.display = 'block'; // Exibe o formulário
});


document
  .getElementById("dogForm")
  .addEventListener("submit", async function (event) {
    event.preventDefault();
    
    // Obtendo os valores do formulário
    const name = document.getElementById("name").value;
    const age = parseInt(document.getElementById("age").value); // Convertendo para número inteiro
    const breed = document.getElementById("breed").value;
    const gender = document.getElementById("gender").value;
    const size = document.getElementById("size").value;
    const profilePictureUrl = document.getElementById("profilePictureUrl").value;
    const description = document.getElementById("description").value;
    const isNeutered = document.getElementById("isNeutered").checked;
    const userId = document.getElementById("userId").value;

    // Verificando se todos os campos obrigatórios foram preenchidos e se a idade é válida
    if (!name || !breed || !gender || !size || !profilePictureUrl || !userId || isNaN(age) || age < 0) {
      alert("Please fill out all fields correctly, especially the age (it cannot be negative).");
      return;
    }

    try {

      const response = await fetch(apiUrlCachorros, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
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
        document.getElementById("addForm").style.display = "none"; // Fechar o formulário após sucesso
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
    preloader.style.display = "flex"; // Exibe o loader
    loader.style.display = "block"; // Exibe o loader animado
  } else {
    preloader.style.display = "none"; // Esconde o loader
    loader.style.display = "none"; // Esconde a animação
  }
}