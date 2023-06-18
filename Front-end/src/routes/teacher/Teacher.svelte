<script>
	export let teacher;
</script>

<h1>Teacher n°{teacher.id}</h1>
<div>
	<h2>{teacher.name}</h2>
	<p>{teacher.surname}</p>
	<p>
	{#each teacher.specialities as speciality}
		{speciality.nameSpeciality}
		<br>
	{/each}
	</p>

</div>

