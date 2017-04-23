<#assign content>
		<h1 id="title"> Life of the Party! </h1>
		
<button id="button"> party!! </button>
<button id="start"> start a playlist!! </button>
<div id="wrapper">
	<div id="grid">
		<div class="btn-arrow btn-arrow-left" id="left"></div>
		<div class="gallery one" id="one">
		    <img src="../photos/dad.jpg" alt="Fjords" width="150" height="125">
		  <div class="desc">Add a description of the image here</div>
		</div>
		
		<div class="gallery two">
		    <img src="fjords.jpg" alt="Fjords" width="150" height="125">
		  <div class="desc">Add a description of the image here</div>
		</div>
		
		<div class="gallery three">
		    <img src="forest.jpg" alt="Forest" width="150" height="125">
		  <div class="desc">Add a description of the image here</div>
		</div>
		
		<div class="gallery two">
		    <img src="lights.jpg" alt="Northern Lights" width="150" height="125">
		  <div class="desc">Add a description of the image here</div>
		</div>
		
		<div class="gallery one">
		    <img src="mountains.jpg" alt="Mountains" width="150" height="125">
		  <div class="desc">Add a description of the image here</div>
		</div>
		<div class="btn-arrow btn-arrow-right" id="right"></div>
	</div>
</div>

<div id="largePhoto">


</div>

<div id="spot">
	<iframe id="box" src="https://open.spotify.com/embed?uri=spotify:user:spotify:playlist:3rgsDhGHZxZ9sB9DQWQfuf" width="300" height="380" frameborder="0" allowtransparency="true"></iframe>
	</div>
</#assign>
<#include "main.ftl">