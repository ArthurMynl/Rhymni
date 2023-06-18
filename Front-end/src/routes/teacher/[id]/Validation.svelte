<script>
	const urlAPI = process.env.URLAPI;
	export let teacher;
	export let userInfo;
	import Cookies from 'js-cookie';
	let teacher_id = teacher.id;
	$: specialitiesStatus = teacher.specialitiesStatus;

	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: 'Bearer ' + Cookies.get('token')
	};

	function validateSpecialities() {
		fetch(urlAPI + `/teacher/changeStatusSpecialities`, {
			method: 'PUT',
			headers: headersList,
			body: JSON.stringify({
				id: teacher_id,
				validate: true
			})
		});
		specialitiesStatus = 'VALIDATED';
	}
	function refuseSpecialities() {
		fetch(urlAPI + `/teacher/changeStatusSpecialities`, {
			method: 'PUT',
			headers: headersList,
			body: JSON.stringify({
				id: teacher_id,
				validate: false
			})
		});
		specialitiesStatus = 'REFUSED';
	}

</script>

<div class="validation">
		<h3>Validation des specialités</h3>

		{#if specialitiesStatus === 'VALIDATED'}
			<div class="validation-group">
				<h4>Specialités Validées</h4>	
				{#if userInfo.role === 'ROLE_OPTION_LEADER'}
					<button on:click={refuseSpecialities} class="validation-button">Refuser</button>
				{/if}					
			</div>
		{:else if specialitiesStatus === 'REFUSED'}
			<div class="validation-group">
				<h4>Specialités refusées</h4>
				{#if userInfo.role === 'ROLE_OPTION_LEADER'}
					<button on:click={validateSpecialities} class="validation-button">Valider</button>				
				{:else}				
					<p>ajoutez les specialités qui vous correspondent</p>		
				{/if}		
			</div>
		{:else}
			<div class="validation-group">
				{#if userInfo.role === 'ROLE_OPTION_LEADER'}
					<button on:click={validateSpecialities} class="validation-button">Valider</button>
					<button on:click={refuseSpecialities} class="validation-button">Refuser</button>
				{/if}
			</div>			
		{/if}
</div>

<style lang="scss">
	.validation {
		display: flex;
		flex-direction: column;
		border-radius: 50px;
		justify-content: center;
		align-items: center;
		width: 50%;
		padding: 50px;
		background-color: white;
		color: black;
		box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);

		.validation-group {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			gap: 10px;
			padding: 10px;

			.validation-button {
				background-color: #3366ff;
				border: none;
				border-radius: 50px;
				color: white;
				padding: 10px 20px;
				font-size: 1em;

				&:hover {
					cursor: pointer;
					background-color: #1a53ff;
				}
			}
		}
	}
</style>
