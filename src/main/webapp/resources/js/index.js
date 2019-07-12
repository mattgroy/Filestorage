let countOfBlocks = 1
let allFiles


function onEditClick(event) {
    let fileId = event.currentTarget.parentElement.parentElement.parentElement.parentElement.getAttribute('fileId')
    window.location.href = '/api/v1/file/' + fileId
}

function onRemoveClick(event) {
    let confirm = window.confirm("Are you sure want to remove file?")
    if(confirm)
    {
        let fileId = event.currentTarget.parentElement.parentElement.parentElement.parentElement.getAttribute('fileId')
        console.log('remove:' + fileId)
        axios.delete('/api/v1/file/'+ fileId, {})
            .then((response) => {
                window.location.href = '/'
            })
            .catch((error) => {
                alert(error)
            })

    }

}

function addFileToList(currentFile)
{
    if(countOfBlocks % 3 === 0)
    {
        let row = $("<div class=\"row presentations\"></div>")
        let table = $("<table style=\"height: 50px;\">\n" +
            "            <tbody>\n" +
            "            <tr>\n" +
            "                <td class=\"align-baseline\"></td>\n" +
            "            </tr>\n" +
            "            </tbody>\n" +
            "        </table>")

        $(".presentation-list").append(row)
        $(".presentation-list").append(table)
    }

       // console.log(allFiles)

            //console.log(currentPresentation)
        let presentationHtml =$("<div class=\"col-lg-4 presentationItem\">\n" +
            "                <div class=\"ih-item square effect6 from_top_and_bottom\">\n" +
            "                    <a href=\"#\" onclick=\"return false\">\n" +
            "                        <div class=\"img\"><img class=\"presentationImage\" src=\"upload/1.jpg\" alt=\"img\"></div>\n" +
            "                        <div class=\"info\">\n" +
            "                            <button type=\"submit\" class=\"btn edit\">\n" +
            "                                <i class=\"fa fa-cloud-download\"></i>\n" +
            "                            </button>\n" +
            "                            <button type=\"submit\" class=\"btn remove confirm\">\n" +
            "                                <i class=\"fa fa-trash-o\"></i>\n" +
            "                            </button>\n" +
            "                            <p class=\"presentationName\"></p>\n" +
            "                        </div>\n" +
            "                    </a>\n" +
            "                </div>\n" +
            "            </div>")

        presentationHtml.attr('id', countOfBlocks.toString())
        presentationHtml.attr('fileId', currentFile.id.toString())


        let body = presentationHtml.children().children()

        let imgClass = body.children('.img')
        let infoClass = body.children('.info')

        imgClass.children().attr('src', "https://cdn.dribbble.com/users/803982/screenshots/2299994/16_-_file_manager.png")
        infoClass.children('.presentationName').html(currentFile.title)




    console.log(body.find('info'))
        presentationHtml.children().children().children().find('info').find('presentationName').html(currentFile.title)

    $(".row.presentations").last().append(presentationHtml)


    countOfBlocks++
}



$(document).ready(function () {
    axios.post('/api/v1/files', {})
        .then((response) => {
            console.log(response)
            allFiles = response.data
            console.log(allFiles)

            allFiles.forEach((currentFile) => {
                addFileToList(currentFile)
            })

            $('.btn.edit').click(onEditClick)

            $('.btn.remove').click(onRemoveClick)

        })
        .catch((error) => {
            console.log(error)
            alert(error)
        })
})
