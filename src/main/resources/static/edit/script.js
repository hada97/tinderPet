let userId = null;

const baseUrl = "http://localhost:8080";
const token = localStorage.getItem("authToken");
const addIcon = document.getElementById("addIcon");
const addForm = document.getElementById("addForm");
const dogCard = document.querySelector(".dog-container");

addIcon.addEventListener("click", () => {
  addIcon.style.display = "none";
  addForm.style.display = "block";
});


// Formulário para registrar um novo cachorro
document
  .getElementById("dogForm")
  .addEventListener("submit", async function (event) {
    event.preventDefault();
    const name = document.getElementById("name").value;
    const age = parseInt(document.getElementById("age").value);
    const breed = document.getElementById("breed").value;


    const token = localStorage.getItem("access_token");

    try {
      const response = await fetch(baseUrl+ edit, {
        method: "PATH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          name,
          phone number,
          address,

        }),
      });

      if (response.ok) {
        alert("uSER successfully UPDATED!");
        document.getElementById("EDITForm").reset();
        addForm.style.display = "none";
        addIcon.style.display = "block";
        await loadDogsPage();
      } else {
        const data = await response.json();
        alert("Error: " + data.message);
      }
    } catch (error) {
      alert(
        "An error occurred while trying to register the USER " + error.message
      );
    }
  });

function logout() {
  alert("Você saiu!");
  window.location.href = "/";
}

//funcao carrega nome
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


// Busca o userId na inicialização
const fetchUserId = async () => {
  try {
    const response = await fetch(`${baseUrl}/users/login/id`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) throw new Error("Falha ao obter o userId");

    userId = await response.json();
    console.log("User ID:", userId);
  } catch (error) {
    console.error("Erro ao buscar userId:", error);
  }
};


// Inicialização ao carregar o DOM
document.addEventListener("DOMContentLoaded", async () => {
  const [userIdResult, dogsResult] = await Promise.all([
    fetchUserId()
  ]);
});

