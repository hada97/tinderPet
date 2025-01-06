let userId = null;

const baseUrl = "http://localhost:8080";
const token = localStorage.getItem("authToken");

const addIcon = document.getElementById("addIcon");
const addForm = document.getElementById("addForm");

// Mostrar o formulário para editar informações do usuário
addIcon.addEventListener("click", () => {
  addIcon.style.display = "none";
  addForm.style.display = "block";
});

// Formulário para editar informações do usuário
document.getElementById("userForm").addEventListener("submit", async function (event) {
  event.preventDefault();

  const name = document.getElementById("name").value;
  const phone = document.getElementById("phone").value;
  const address = document.getElementById("address").value;

  const token = localStorage.getItem("authToken");

  try {
    const response = await fetch(`${baseUrl}/users`, {  // Endpoint ajustado para '/users'
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        name,      // Nome
        phone,     // Telefone
        address,   // Endereço
      }),
    });

    if (response.ok) {
      alert("Informações do usuário atualizadas com sucesso!");
      document.getElementById("userForm").reset();
      addForm.style.display = "none";
      addIcon.style.display = "block";
    } else {
      const data = await response.json();
      alert("Erro: " + data.message);
    }
  } catch (error) {
    alert("Ocorreu um erro ao tentar atualizar o usuário: " + error.message);
  }
});

// Função para fazer logout
function logout() {
  alert("Você saiu!");
  window.location.href = "/";
}

// Função para carregar o nome do usuário
fetch(`${baseUrl}/api/user/profile`, {
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
  await fetchUserId();  // Espera o userId ser carregado
});
