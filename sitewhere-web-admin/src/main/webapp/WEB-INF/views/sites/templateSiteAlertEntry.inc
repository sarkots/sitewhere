<!-- Template for alert row -->
<script type="text/x-kendo-tmpl" id="tpl-alert-entry">
	<tr class="sw-list-entry">
		<td class="ellipsis" style="max-width: 90px;" title="#:assetName#">
			<div>
				<span class="ellipsis" style="max-width: 150px; display: inline-block; vertical-align: middle; line-height: normal">
					#:assetName#</span>
				<i class="fa fa-circle-arrow-right sw-action-glyph sw-view-glyph" title="#= i18next("public.ViewAssignment") #"
					style="vertical-align: middle;" onclick="onViewAssignment(event, '#:deviceAssignmentToken#')"></i>
			</div>
		</td>
		<td class="ellipsis" style="max-width: 90px;" title="#:type#">#:type#</td>
		<td class="ellipsis" style="max-width: 250px;" title="#:message#">#:message#</td>
		<td class="ellipsis">#:source#</td>
		<td>#= formattedDate(eventDate) #</td>
		<td>#= formattedDate(receivedDate) #</td>
	</tr>
</script>