<script>
	const urlAPI = process.env.URLAPI;
	let fileInput;

	import Cookies from 'js-cookie';
	import { toast } from '@zerodevx/svelte-toast';

	import { createEventDispatcher } from 'svelte';
	const dispatch = createEventDispatcher();


	const headersList = {
		Authorization: 'Bearer ' + Cookies.get('token')
	};

	async function handleFileChange(event) {
		let file = event.target.files[0];
		if (!file) return;

		const formData = new FormData();
		formData.append('file', file);
		let response = await fetch(urlAPI + `/student/import`, {
			method: 'POST',
			headers: headersList,
			body: formData
		});

		fileInput.value = '';

        if (response.status === 201) {
            toast.push('Le fichier a bien été importé', {
			theme: {
				'--toastBackground': '#3366ff',
				'--toastProgressBackground': '#fff',
				'--toastBorderRadius': '15px'
			},
			pausable: true
		});
        } else {
            toast.push("Erreur d'importation, veuillez essayer avec un autre fichier", {
			theme: {
				'--toastBackground': '#eb4e3f',
				'--toastProgressBackground': '#fff',
				'--toastBorderRadius': '15px'
			},
			pausable: true
		});
        }
		dispatch('import');
	}
</script>

<div class="container">
	<label for="file" class="label-file">Importer des étudiants</label>
	<input id="file" type="file" bind:this={fileInput} on:change={handleFileChange} />
</div>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;

		input {
			display: none;
		}

		.label-file {
			cursor: pointer;
			border-radius: 10px;
			padding: 10px;
			background-color: #3366ff;
			color: white;
		}
	}
</style>
