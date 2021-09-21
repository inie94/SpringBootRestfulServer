fetch('/edit-profile', {
                method: 'POST', // *GET, POST, PUT, DELETE, etc.
////            mode: 'cors', // no-cors, *cors, same-origin
////            cache: 'force-cache', // *default, no-cache, reload, force-cache, only-if-cached
////            credentials: 'include', // include, *same-origin, omit
                headers: {
                    [csrfHeader] : csrfToken,
                    'charset': 'UTF-8',
                    'Content-Type': 'application/json'
//              // 'Content-Type': 'application/x-www-form-urlencoded',
                },
                redirect: 'follow', // manual, *follow, error
//            referrerPolicy: 'no-referrer', // no-referrer, *client
            body: JSON.stringify(profile) // body data type must match "Content-Type" header
          }).then(res => console.log(res)).catch(err => console.log(err));