<script>
	const urlAPI = process.env.URLAPI;
	export let project;
	import Cookies from 'js-cookie';
	import { invalidateAll } from '$app/navigation';
	import { toast } from '@zerodevx/svelte-toast';

	let fileInput;
	let name = `Rapport ${project.id}.pdf`;

	async function handleFileChange(event) {
		let file = event.target.files[0];
		
		if (!file) return;

		if (file.type !== 'application/pdf') {
			toast.push("Veuillez sélectionner un fichier PDF", {
			theme: {
				'--toastBackground': '#ff0000',
				'--toastProgressBackground': '#fff',
				'--toastBorderRadius': '15px'
			},
			pausable: true
			});
			return;
		}
		const formData = new FormData();
		formData.append('file', file);
		formData.append('name', name);

		let response = await fetch(urlAPI + `/file/uploadFile`, {
			method: 'POST',
			headers: {
				Authorization: 'Bearer ' + Cookies.get('token')
			},
			body: formData
		});
		if (response.ok) {
			toast.push('Le rapport a bien été importé', {
			theme: {
				'--toastBackground': '#3366ff',
				'--toastProgressBackground': '#fff',
				'--toastBorderRadius': '15px'
			},
			pausable: true
		})
		} else {
			toast.push("Erreur d'importation, veuillez essayer avec un autre fichier", {
			theme: {
				'--toastBackground': '#3366ff',
				'--toastProgressBackground': '#fff',
				'--toastBorderRadius': '15px'
			},
			pausable: true
		});
		}
		fileInput.value = '';
		invalidateAll();
	}
</script>

<div class="container">
	<label for="report" class="label-file">Importer un rapport</label>
	<input id="report" type="file" bind:this={fileInput} on:change={handleFileChange} />
</div>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;
		width: 100%;

		input {
			display: none;
		}

		.label-file {
			cursor: pointer;
			border-radius: 10px;
			padding: 10px;
			background-color: #3366ff;
			color: white;
			width: 100%;
			text-align: center;
		}
	}
</style>
