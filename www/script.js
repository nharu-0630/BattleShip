function HpArrayToTable(id, array) {
    let table = document.getElementById(id);
    for (let index = 0; index < array.length; index++) {
        const element = array[index];
        switch (element) {
            case 3:
                table.getElementsByTagName("td")[index].setAttribute("class", "table-success");
                break;
            case 2:
                table.getElementsByTagName("td")[index].setAttribute("class", "table-warning");
                break;
            case 1:
                table.getElementsByTagName("td")[index].setAttribute("class", "table-danger");
                break;
            case 0:
                table.getElementsByTagName("td")[index].setAttribute("class", "table-dark");
                break;
            default:
                table.getElementsByTagName("td")[index].setAttribute("class", "");
                break;
        }

    }
}

function ValueArrayToTable(id, array) {
    let table = document.getElementById(id);
    for (let index = 0; index < array.length; index++) {
        const element = array[index];
        switch (element) {
            default:
                table.getElementsByTagName("td")[index].innerText = element;
                break;
        }

    }
}

function IsAttackArrayToTable(id, array) {
    let table = document.getElementById(id);
    for (let index = 0; index < array.length; index++) {
        const element = array[index];
        switch (element) {
            case true:
                table.getElementsByTagName("td")[index].setAttribute("class", "table-danger");
                break;
            case false:
                table.getElementsByTagName("td")[index].setAttribute("class", "");
                break;
            default:
                table.getElementsByTagName("td")[index].setAttribute("class", "");
                break;
        }

    }
}

function TableRefresh() {
    HpArrayToTable("alphaHp", json[document.getElementById("progressRange").value][true]["hp"]);
    ValueArrayToTable("alphaValue", json[document.getElementById("progressRange").value][true]["value"]);
    IsAttackArrayToTable("alphaIsAttack", json[document.getElementById("progressRange").value][true]["isAttack"]);
    HpArrayToTable("bravoHp", json[document.getElementById("progressRange").value][false]["hp"]);
    ValueArrayToTable("bravoValue", json[document.getElementById("progressRange").value][false]["value"]);
    IsAttackArrayToTable("bravoIsAttack", json[document.getElementById("progressRange").value][false]["isAttack"]);
}

let json;

document.getElementById("previousButton").addEventListener('click', function () {
    if (document.getElementById("progressRange").value !== 0) {
        document.getElementById("progressRange").value--;
        TableRefresh();
    }
});

document.getElementById("nextButton").addEventListener('click', function () {
    if (document.getElementById("progressRange").value !== document.getElementById("progressRange").attributes["max"]) {
        document.getElementById("progressRange").value++;
        TableRefresh();
    }
});

document.getElementById("progressRange").addEventListener('input', function () {
    TableRefresh();
});

document.getElementById("fileInput").addEventListener('change', function () {
    let fileReader = new FileReader();
    fileReader.readAsText(document.getElementById("fileInput").files[0]);
    fileReader.addEventListener("load", function () {
        json = JSON.parse(fileReader.result);
        document.getElementById("progressRange").value = 1;
        document.getElementById("progressRange").setAttribute("max", Object.keys(json).length);
    });
});