function setRowAndNumber() {
    console.log(localStorage.getItem('row'), localStorage.getItem('number'))
    document.getElementById('row').innerText = localStorage.getItem('row');
    document.getElementById('number').innerText = localStorage.getItem('number');
}

function sendData() {
    if (!validateInput()) {
        return;
    }
    let message = createJsonMessage();
    console.log(message)
    fetch('orders', {
        method : 'POST',
        body : message
    }).then(response => {
        return response.json();
    })
      .then(data => {
        let message = data.message;
            let div = document.getElementById('status-message');
            div.setAttribute('class', 'd-block');
            div.innerText = message;
    })
}

function createJsonMessage() {
    let message = {name : document.getElementById('username').value,
        phone : document.getElementById('phone').value,
        row : localStorage.getItem('row'),
        number : localStorage.getItem('number')};
        return JSON.stringify(message);
}

function validateInput() {
    let els = document.getElementsByTagName('input');
    let rslString = '';
    for (let i = 0; i < els.length; i++) {
        let element = els[i];
        if (element.getAttribute('type') === 'text'
            && element.value === '') {
            rslString += `Заполните поле ${element.labels[0].innerText}\n`;
        }
    }
    if (rslString !== '') {
        alert(rslString);
        return false;
    }
    return true;
}
document.getElementById('pay-btn').addEventListener('click', sendData)
setRowAndNumber();