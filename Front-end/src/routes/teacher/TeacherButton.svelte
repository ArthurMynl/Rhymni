<script>
	export let teacher;
</script>

<div class="card-teacher">
	<h2>{teacher.name} {teacher.surname}</h2>
	
	<a class="smallButton" href="/teacher/{teacher.id}" id="seeTeacher{teacher.id}">Voir profil</a>
</div>

<style>

	.card-teacher {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
		border-radius: 50px;
		background-color: white;
		color: black;
		padding: 2% 5% 2% 5%;
		box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
		width: 600px;
		height: 200px;
	}


	.smallButton {
		padding: 10px 10px;
		text-align: center;
		width: 100px;
		border-radius: 10px;
		color: white;
		background-color: #3366ff;
		border: none;
		font-size: 1em;
		margin: 5% 0 0 0;
		text-decoration: none;
	}
</style>
