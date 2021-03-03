function refreshCell(place) {
    let table = document.getElementById('hall-table');
    table.rows[place.row]
        .cells[place.number]
        .children[0].disabled = !place.available;
}

async function refreshWithTimeOut(time) {
    while(true) {
      await fetch('places')
          .then(response => {
              return response.json()
          })
          .then(jsonData => {
              jsonData.forEach(x => {
                  refreshCell(x)
              })
          })
          .then(() => {
            setTimeout(time);
        });
    }
}

function sendToPurchaseForm() {
    let cell = getSelectedCell();
    if (cell === null) {
        alert("Выберите место");
        return;
    }
    let row = cell.parentElement;
    console.log(row.rowIndex, cell.cellIndex, cell.innerText)
    localStorage.setItem('row', row.rowIndex)
    localStorage.setItem('number', cell.cellIndex)
    window.location.href = "purchase.html"
}

function getSelectedCell() {
    let radios = document.querySelectorAll('[type="radio"]');
    console.log(radios)
    let rsl = null;
    radios.forEach(x => {
        if (x.checked) {
            rsl = x.parentElement;
        }
    })
    return rsl;
}

refreshWithTimeOut(5000);
document.querySelector('#send-btn')
    .addEventListener('click', sendToPurchaseForm);

