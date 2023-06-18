<script>
	export let teacher;
</script>

<div class="card-teacher">
	<h2>{teacher.name} {teacher.surname}</h2>
	{#if teacher.specialities.length === 0}
		<h3>Ce professeur n'a pas de spécialité</h3>
	{:else}
		<h3>Specialités :</h3>
	{/if}
	<div class="list-specialities" id="specialitiesTeacher">
		{#each teacher.specialities as speciality}
			{#if speciality.nameSpeciality === 'MOD'}
				<p>Modélisation</p>
			{:else if speciality.nameSpeciality === 'DEV'}
				<p>Développement</p>
			{:else if speciality.nameSpeciality === 'INF'}
				<p>Infrastructure</p>
			{/if}
		{/each}
	</div>
</div>

<style>
	p {
		margin-block-start: 0;
		margin-block-end: 0;
	}

	h3 {
		margin-top: 0.3rem;
		margin-bottom: 0.5rem;
	}

	.card-teacher {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
		border-radius: 50px;
		background-color: white;
		color: black;
		padding: 2% 5% 2% 5%;
		margin: 0 0% 1% 0%;
		box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
		width: 50%;
	}

	.list-specialities {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: flex-start;
		gap: 0.3rem;
	}
</style>
