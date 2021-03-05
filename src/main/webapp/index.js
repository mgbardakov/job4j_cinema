// function refreshCell(place) {
//     let table = document.getElementById('hall-table');
//     table.rows[place.row]
//         .cells[place.number]
//         .children[0].disabled = !place.available;
// }

async function refreshWithTimeOut(time) {
    while(true) {
      await fetch('orders')
          .then(response => {
              return response.json()
          })
          .then(orders => {
              let table = document.getElementById('hall-table');
              clearTable(table);
              disableReservedSits(table, orders)
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

function clearTable(table) {
    //let table = document.getElementById('hall-table');
    for (let i = 1; i < table.rows; i++) {
        let row = table.rows[i];
        for (let j = 1; j < row.cells; j++) {
            let cell = row.cells[j];
            cell.children[0].disabled = false;
        }
    }
}

function disableReservedSits(table, orders) {
   for (let order of orders) {
       table.rows[order.row].cells[order.number].children[0].disabled = true;
   }
}

refreshWithTimeOut(5000);
document.querySelector('#send-btn')
    .addEventListener('click', sendToPurchaseForm);

