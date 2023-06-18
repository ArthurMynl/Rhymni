<script>
	import TeamPair from './TeamPair.svelte';
	import CreatePair from './CreatePair.svelte';
	import { invalidateAll } from '$app/navigation';
	export let data;
	$: pairData = data.pairData;
	$: teamData = data.teamData;
	let userInfo = data.userInfo;

	import { goto } from '$app/navigation';
	import Navbar from '../Navbar.svelte';

	$: if (typeof window !== 'undefined') {
		if (data.status === 401) {
			goto('/auth', { invalidateAll: true });
		}
	}

	async function refreshData() {
		invalidateAll();
	}
</script>

<Navbar {userInfo} />
<div class="container">
	<div class="card-team-pairs">
		{#if pairData.length === 0}
			<h2>Aucune paire d'équipe</h2>
		{:else}
			<h2>Liste des paires d'équipe</h2>
			{#each pairData as teamPair}
				<TeamPair
					team1={teamPair[0].number}
					team2={teamPair[1].number}
					{userInfo}
					on:pairDeleted={refreshData}
				/>
			{/each}
		{/if}
	</div>

	{#if userInfo.role === 'ROLE_OPTION_LEADER'}
		<div class="card-create-pair">
			<h2>Créer une paire d'équipe</h2>
			<CreatePair teams={teamData} on:pairCreated={refreshData} />
		</div>
	{/if}
</div>

<style>
	.container {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
		gap: 50px;
		margin: 50px 0;
	}

	.card-team-pairs {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
		width: 40%;
		border-radius: 30px;
		background-color: white;
		color: black;
		gap: 10px;
		padding: 2% 10% 4% 10%;
		box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
	}
	.card-create-pair {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
		width: 40%;
		border-radius: 30px;
		background-color: white;
		color: black;
		padding: 2% 10% 4% 10%;
		box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
	}
</style>
