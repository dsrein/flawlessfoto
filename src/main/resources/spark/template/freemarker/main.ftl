<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity.
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/html5bp.css"> -->
    <link rel="stylesheet" href="css/main.css">
    <link href="https://fonts.googleapis.com/css?family=Bungee+Shade" rel="stylesheet">
  </head>
  <body>
     ${content}
     <!-- Again, we're serving up the unminified source for clarity. -->
     <script src="js/jquery-2.1.1.js"></script>
     <script src="js/main.js"></script>

     <!-- Button -->
     <script>
     window.ButtonWebConfig = {
       applicationId: 'app-2294a462505cef71',
       defaults: {
         'btn-4e680ef15e505fe9': {
           context: {
             item: {
               identifiers: {
                 itunes: 'id1419227' // Beyonce
               }
             }
           }
         }
       }
      };
      (function(u,s,e,b,t,n){
        u['__bttnio']=b;u[b]=u[b]||function(){(u[b].q=u[b].q||[]).push(arguments)};t=s.createElement(e);n=s.getElementsByTagName(e)[0];t.async=1;t.src='https://web.btncdn.com/v1/button.js';n.parentNode.insertBefore(t,n)
      })(window, document, 'script', 'bttnio');
      </script>

  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
