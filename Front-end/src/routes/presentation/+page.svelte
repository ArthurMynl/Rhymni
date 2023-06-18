<script>
	const urlAPI = process.env.URLAPI;
	import Cookies from 'js-cookie';
	export let data;
	let dataPresentation = data.dataPresentation;
	let dataRoom = data.dataRoom;
	let pairTeacherData = data.pairTeacherData;
	let dataTeam = data.dataTeam;
	let headersList = {
		'Content-Type': 'application/json',
		Authorization: 'Bearer ' + Cookies.get('token')
	};
	const userInfo = data.userInfo;
	import PresentationDetail from '../../lib/PresentationDetail.svelte';
	import PresentationCreate from '../../lib/PresentationCreate.svelte';
	import Navbar from '../Navbar.svelte';
	import PresentationModify from '../../lib/PresentationModify.svelte';
	import PresentationLabel from '../../lib/PresentationLabel.svelte';

	import { goto } from '$app/navigation';
	import { toast } from '@zerodevx/svelte-toast';

	$: if (typeof window !== 'undefined') {
		if (data.status === 401) {
			goto('/auth', { invalidateAll: true });
		}
	}

	$: showPanelPresentation = -1;

	let presentations = dataPresentation;
	let rooms = dataRoom;
	let teachers = pairTeacherData;
	let teams = dataTeam;

	function deletePresentation(event) {
		let id = event.detail.id;
		fetch(urlAPI + `/planning/deleteIntermediatePresentation`, {
			method: 'POST',
			headers: headersList,
			body: JSON.stringify({
				idPresentation: id,
				date: null,
				hours: null,
				idRoom: null,
				idTeacher: null,
				idTeam: null
			})
		})
			.then((response) => {
				if (response.status === 200) {
					window.location.reload();
				} else if (response.status === 400) {
					throw new Error(
						"L'équipe à une soutenance finale, supprimez là avant de supprimer la soutenance intermédiaire."
					);
				} else {
					throw new Error("Une erreur inattendue s'est produite.");
				}
			})
			.catch((error) => {
				toast.push(error.message, {
					theme: {
						'--toastBackground': '#eb4e3f',
						'--toastProgressBackground': '#ff0000',
						'--toastProgressAfterBackground': '#ff0000',
						'--toastIconFill': '#fff',
						'--toastBorderRadius': '15px'
					},
					pausable: true
				});
			});
	}

	function displayHideSidePanelPresentation(pres) {
		if (pres.idPresentation === showPanelPresentation.idPresentation) {
			showPanelPresentation = -1;
		} else {
			showPanelPresentation = pres;
		}
	}

	function getPresentationByID(id) {
		for (let i = 0; i < presentations.length; i++) {
			if (id === presentations[i].idPresentation) {
				return presentations[i];
			}
		}
	}
</script>

<Navbar {userInfo} />
<div class="container">
	<div class="presentation-list">
		<h1>Liste des soutenances</h1>
		{#each presentations as presentation}
			<button
				class="btn-presentation"
				id="display-{presentation.teams[0].number}-{presentation.type}"
				on:click={() => {
					displayHideSidePanelPresentation(presentation);
				}}
			>
				<PresentationLabel {presentation} />
			</button>
		{/each}
	</div>

	{#if showPanelPresentation !== -1}
		<div class="presentation-card">
			<PresentationDetail presentation={showPanelPresentation} {userInfo} />
			{#if userInfo.role === 'ROLE_PLANNING_ASSISTANT'}
				<PresentationModify
					presentation={showPanelPresentation}
					{rooms}
					{teachers}
					on:deletePresentation={deletePresentation}
				/>
			{/if}
		</div>
	{:else if userInfo.role === 'ROLE_PLANNING_ASSISTANT'}
		<PresentationCreate {rooms} {teachers} {teams} />
	{/if}
</div>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: row;
		width: 100%;
		justify-content: flex-start;
		align-items: flex-start;
		margin: 50px 0;
		padding-left: 50px;
		padding-right: 50px;
		gap: 50px;

		.presentation-list {
			display: flex;
			flex-direction: column;
			width: 30%;
			gap: 20px;
			box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
			padding: 3% 3%;
			border-radius: 20px;

			.btn-presentation {
				background-color: #3366ff;
				border: none;
				color: white;
				cursor: pointer;
				width: 100%;
				text-align: center;
				padding: 10px;
				border-radius: 10px;
			}

			h1 {
				text-align: center;
			}
		}

		.presentation-card {
			box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
			padding: 3% 3%;
			border-radius: 20px;
			width: calc(70% - 50px);
			display: flex;
			flex-direction: column;
			gap: 10px;
		}
	}
</style>
