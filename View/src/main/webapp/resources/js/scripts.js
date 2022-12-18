function validatePrice(message) {
    const price = document.getElementById("createProduct:inputprice").value;
    const priceRegex = new RegExp('^\\d{0,8}(\\.\\d{1,4})?$');
    if (!priceRegex.test(price)) {
        alert(message);
        return false;
    }
    return true;
}

function validatePrice2(message) {
    const price = document.getElementById("tableForm:table").getElementsByTagName("input").item(0);
    const priceRegex = new RegExp('^\\d{0,8}(\\.\\d{1,4})?$');
    if (!priceRegex.test(price.value)) {
        alert(message);
        return false;
    }
    return confirm();
}

function getFiltered(username, email, active, id) {
    let table = $("#table");
    table.empty();
    let header = $("<tr></tr>");
    header.append($("<td></td>").append(username));
    header.append($("<td></td>").append(email));
    header.append($("<td></td>").append(active));
    header.append($("<td></td>").append(id));
    table.append(header);
    let filter = document.getElementById("form:inputFilterData").value;
    if (filter == null) {
        filter = "";
    }
    $.get("http://localhost:8081/rest/api/customers", {username: filter}, function(data) {
       let array = JSON.parse(JSON.stringify(data));
       for(let i = 0; i < array.length; i++) {
           let row = $("<tr></tr>");
           row.append($("<td></td>").append(array[i].username));
           row.append($("<td></td>").append(array[i].email));
           row.append($("<td></td>").append(array[i].active));
           row.append($("<td></td>").append(array[i].customerID));
           table.append(row);
       }
    });
}
