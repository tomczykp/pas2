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
