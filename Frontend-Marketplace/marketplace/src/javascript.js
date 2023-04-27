function register() {
    document.getElementById("camposFornecedor").style.display = "none";

    document.getElementById("submitBtn").addEventListener("click", function () {
        let value = document.getElementById("filtraSelect").value;

        // if (value === "consumidor") {
        //     window.location.href = '/welcome';
        // } else if (value === "fornecedor") {
        //     window.location.href = '/admin-fornecedor';
        // }
    });

    document.getElementById("filtraSelect").addEventListener("change", function () {
        let tipo = this.value;
        if (tipo === "consumidor") {
            document.getElementById("camposConsumidor").style.display = "flex";
            document.getElementById("camposFornecedor").style.display = "none";
        } else if (tipo === "fornecedor") {
            document.getElementById("camposConsumidor").style.display = "none";
            document.getElementById("camposFornecedor").style.display = "flex";
        } else {
            document.getElementById("camposConsumidor").style.display = "none";
            document.getElementById("camposFornecedor").style.display = "none";
        }
    });

}
