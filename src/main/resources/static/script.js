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
